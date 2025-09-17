//package com.example.userldap.user;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ldap.core.AttributesMapper;
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.ldap.filter.AndFilter;
//import org.springframework.ldap.filter.EqualsFilter;
//import org.springframework.ldap.support.LdapNameBuilder;
//import org.springframework.stereotype.Service;
//import javax.naming.Name;
//import javax.naming.directory.BasicAttribute;
//import javax.naming.directory.BasicAttributes;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.regex.Pattern;
//@Service
//public class UserService {
//    // 2 more components that are injected via @Autowired
//    @Autowired
//    // Tool that handles all complex ldap commands
//    private LdapTemplate ldapTemplate;
//    // Interface that provides methods to interact with HistoricalData
//    @Autowired
//    private WeatherRepository weatherRepository;
//
//    //Method to verify user login cred again ldap server
//    public UserDTO authenticate(String emailid, String rawPassword) {
//        // Used to define search criteria (like a WHERE clause in SQL)
//        AndFilter filter = new AndFilter();
//
//        // First condition: the object's class must be 'inetOrgPerson'
//        filter.and(new EqualsFilter("objectClass", "inetOrgPerson"));
//
//        // Second condition: the 'mail' attribute must match the provided email
//        filter.and(new EqualsFilter("mail", emailid));
//
//        try {
//            // Attempt to authenticate with the LDAP server using the filter and password
//            boolean isAuthenticated = ldapTemplate.authenticate("", filter.encode(), rawPassword);
//
//            // If authentication fails, log and return null
//            if (!isAuthenticated) {
//                System.out.println("Authentication failed for email: " + emailid);
//                return null;
//            }
//
//            // If authentication is successful, search for the user's details
//            List<UserDTO> result = ldapTemplate.search(
//                    "",
//                    filter.encode(),
//                    // A lambda function to map LDAP attributes to a UserDTO object
//                    (AttributesMapper<UserDTO>) attrs -> {
//                        UserDTO user = new UserDTO();
//                        // Get the user's email from the 'mail' attribute
//                        user.setEmailid((String) attrs.get("mail").get());
//                        // Get the user's full name from the 'cn' (common name) attribute
//                        user.setFullName((String) attrs.get("cn").get());
//                        return user;
//                    }
//            );
//
//            // Ternary operator to check if the search returned a user; if so, return the user
//            return result.isEmpty() ? null : result.get(0);
//
//        } catch (Exception e) {
//            // Catch any unexpected errors during LDAP connection or authentication
//            System.err.println("Error during LDAP authentication for email " + emailid + ": " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // Method for creating new user entry
//    public UserDTO addUser(UserDTO userDTO) {
//        // Validate user input before processing
//        validateUserDTO(userDTO);
//        String fullName = userDTO.getFullName().trim();
//
//        // Check if a user with this email already exists before adding
//        if (userExists(userDTO.getEmailid())) {
//            // If a duplicate is found, throw an exception
//            throw new RuntimeException("This email is already registered.");
//        }
//
//        // Constructing LDAP entry's unique identifier (DN)
//        Name dn = LdapNameBuilder.newInstance()
//               // .add("ou", "ProjectTeam1")
//                .add("uid", fullName)
//                .build();
//
//        // BasicAttributes to hold the user's data
//        BasicAttributes attrs = new BasicAttributes();
//        BasicAttribute objectClass = new BasicAttribute("objectClass");
//        objectClass.add("inetOrgPerson");
//        objectClass.add("organizationalPerson");
//        objectClass.add("person");
//        objectClass.add("top");
//        //add all objclass attr to main container attrs
//        attrs.put(objectClass);
//
//        // Add other attributes for the new user
//        attrs.put("uid", fullName);
//        attrs.put("cn", fullName);
//        attrs.put("sn", fullName);
//        attrs.put("mail", userDTO.getEmailid());
//        attrs.put("userPassword", userDTO.getPassword());
//
//        // Use LdapTemplate to add the new entry to the directory
//        ldapTemplate.bind(dn, null, attrs);
//
//        System.out.println("User created successfully: " + fullName);
//        return userDTO;
//    }
//
//    // Private helper method used by addUser to check for duplication
//    private boolean userExists(String emailid) {
//        // AndFilter to search for LDAP entry that meets the conditions below
//        AndFilter filter = new AndFilter();
//        // Condition 1: objectClass must be 'inetOrgPerson'
//        filter.and(new EqualsFilter("objectClass", "inetOrgPerson"));
//        // Condition 2: 'mail' attribute must match the provided email
//        filter.and(new EqualsFilter("mail", emailid));
//
//        try {
//            // Search the LDAP directory for a matching user entry
//            List<String> userDn = ldapTemplate.search(
//                    "",
//                    filter.encode(),
//                    // Map the search result to a list of email strings
//                    (AttributesMapper<String>) attrs -> (String) attrs.get("mail").get()
//            );
//
//            // Return true if any user was found, false otherwise
//            return !userDn.isEmpty();
//        } catch (Exception e) {
//            // Handle any errors during the search and return false
//            System.err.println("Error checking for duplicate user with email " + emailid + ": " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//    private void validateUserDTO(UserDTO userDTO) {
//        // Simple email regex pattern
//        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
//        Pattern emailPattern = Pattern.compile(emailRegex);
//        // Password regex for a strong password
//        // At least 8 characters, at least one uppercase letter, one lowercase letter, one number and one special character
//        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
//        Pattern passwordPattern = Pattern.compile(passwordRegex);
//        // New regex for Full Name: Only letters, numbers, and spaces
//        String fullNameRegex = "^[a-zA-Z0-9 ]+$";
//        Pattern fullNamePattern = Pattern.compile(fullNameRegex);
//        if (!emailPattern.matcher(userDTO.getEmailid()).matches()) {
//            throw new RuntimeException("Invalid email address format.");
//        }
//        //check if the full name is longer then 20 char
//        if (userDTO.getFullName().trim().length() > 20) {
//            throw new RuntimeException("Full Name must be up to 20 characters long.");
//        }
//        // New validation check for full name
//        if (!fullNamePattern.matcher(userDTO.getFullName().trim()).matches()) {
//            throw new RuntimeException("Full Name cannot contain special characters.");
//        }
//        //Check if the password meets the strength requirement
//        if (!passwordPattern.matcher(userDTO.getPassword()).matches()) {
//            throw new RuntimeException("Password must be 8-20 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.");
//        }
//    }
//    //Method to retrive historical data from db
//    public List<HistoricalData> getHistoricalDataByCityName(String cityName, LocalDate startDate, LocalDate endDate) {
//        //uses weatrepo to find data for a city within range
//        return weatherRepository.findByCityNameIgnoreCaseAndDateBetween(cityName, startDate, endDate);
//    }
//}


package com.example.userldap.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    // 2 more components that are injected via @Autowired
    @Autowired
    // Tool that handles all complex ldap commands
    private LdapTemplate ldapTemplate;
    // Interface that provides methods to interact with HistoricalData
    @Autowired
    private WeatherRepository weatherRepository;

    //Method to verify user login cred again ldap server
    public UserDTO authenticate(String emailid, String rawPassword) {
        // Used to define search criteria (like a WHERE clause in SQL)
        AndFilter filter = new AndFilter();

        // First condition: the object's class must be 'inetOrgPerson'
        filter.and(new EqualsFilter("objectClass", "inetOrgPerson"));

        // Second condition: the 'mail' attribute must match the provided email
        filter.and(new EqualsFilter("mail", emailid));

        try {
            // Attempt to authenticate with the LDAP server using the filter and password
            boolean isAuthenticated = ldapTemplate.authenticate("", filter.encode(), rawPassword);

            // If authentication fails, log and return null
            if (!isAuthenticated) {
                System.out.println("Authentication failed for email: " + emailid);
                return null;
            }

           // This will return an empty UserDTO to the client.
            return new UserDTO();

        } catch (Exception e) {
            // Catch any unexpected errors during LDAP connection or authentication
            System.err.println("Error during LDAP authentication for email " + emailid + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Method for creating new user entry
    public UserDTO addUser(UserDTO userDTO) {
        // Validate user input before processing
        validateUserDTO(userDTO);
        String fullName = userDTO.getFullName().trim();

        // Check if a user with this email already exists before adding
        if (userExists(userDTO.getEmailid())) {
            // If a duplicate is found, throw an exception
            throw new RuntimeException("This email is already registered.");
        }

        // Constructing LDAP entry's unique identifier (DN)
        Name dn = LdapNameBuilder.newInstance()
                // .add("ou", "ProjectTeam1")
                .add("uid", fullName)
                .build();

        // BasicAttributes to hold the user's data
        BasicAttributes attrs = new BasicAttributes();
        BasicAttribute objectClass = new BasicAttribute("objectClass");
        objectClass.add("inetOrgPerson");
        objectClass.add("organizationalPerson");
        objectClass.add("person");
        objectClass.add("top");
        //add all objclass attr to main container attrs
        attrs.put(objectClass);

        // Add other attributes for the new user
        attrs.put("uid", fullName);
        attrs.put("cn", fullName);
        attrs.put("sn", fullName);
        attrs.put("mail", userDTO.getEmailid());
        attrs.put("userPassword", userDTO.getPassword());

        // Use LdapTemplate to add the new entry to the directory
        ldapTemplate.bind(dn, null, attrs);

        System.out.println("User created successfully: " + fullName);
        return userDTO;
    }

    // Private helper method used by addUser to check for duplication
    private boolean userExists(String emailid) {
        // AndFilter to search for LDAP entry that meets the conditions below
        AndFilter filter = new AndFilter();
        // Condition 1: objectClass must be 'inetOrgPerson'
        filter.and(new EqualsFilter("objectClass", "inetOrgPerson"));
        // Condition 2: 'mail' attribute must match the provided email
        filter.and(new EqualsFilter("mail", emailid));

        try {
            // Search the LDAP directory for a matching user entry
            List<String> userDn = ldapTemplate.search(
                    "",
                    filter.encode(),
                    // Map the search result to a list of email strings
                    (AttributesMapper<String>) attrs -> (String) attrs.get("mail").get()
            );

            // Return true if any user was found, false otherwise
            return !userDn.isEmpty();
        } catch (Exception e) {
            // Handle any errors during the search and return false
            System.err.println("Error checking for duplicate user with email " + emailid + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private void validateUserDTO(UserDTO userDTO) {
        // Simple email regex pattern
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        // Password regex for a strong password
        // At least 8 characters, at least one uppercase letter, one lowercase letter, one number and one special character
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        // New regex for Full Name: Only letters, numbers, and spaces
        String fullNameRegex = "^[a-zA-Z0-9 ]+$";
        Pattern fullNamePattern = Pattern.compile(fullNameRegex);
        if (!emailPattern.matcher(userDTO.getEmailid()).matches()) {
            throw new RuntimeException("Invalid email address format.");
        }
        //check if the full name is longer then 20 char
        if (userDTO.getFullName().trim().length() > 20) {
            throw new RuntimeException("Full Name must be up to 20 characters long.");
        }
        // New validation check for full name
        if (!fullNamePattern.matcher(userDTO.getFullName().trim()).matches()) {
            throw new RuntimeException("Full Name cannot contain special characters.");
        }
        //Check if the password meets the strength requirement
        if (!passwordPattern.matcher(userDTO.getPassword()).matches()) {
            throw new RuntimeException("Password must be 8-20 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.");
        }
    }
    //Method to retrive historical data from db
    public List<HistoricalData> getHistoricalDataByCityName(String cityName, LocalDate startDate, LocalDate endDate) {
        //uses weatrepo to find data for a city within range
        return weatherRepository.findByCityNameIgnoreCaseAndDateBetween(cityName, startDate, endDate);
    }
}