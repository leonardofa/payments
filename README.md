This project is a solution for a payment provider where is possible to create 
Users, Accounts, Products and payments


What needs to be done?
	-> Correct bugs on application
	-> Refactoring payment flux
	-> Create tests for payment flux
	-> delivery in bitbucket, GitHub or gitlab
	


process ->
	create payment -> confirm payment


## Donne and increase:
1. Fork project on GitHub
2. Using Lombok
3. Using Openapi (default: http://localhost:8080/swagger-ui/index.html)
4. Using Spring Validation
5. Using ModelMapper
6. Active Jackson deserialization exception to use *ApiExceptionHandler*
7. Create entity *PaymentProduct* for using quantity column
8. Change mapping account_id to product_id on *PaymentProduct*
9. Using @PrePersist to calculate total_price for Payment
10. Using ModelMapper to calculate Payment Hash see *PaymentOutputConverter* class
11. Exceptions created for use in *ApiExceptionHandler*
12. Models Create for input and output
13. Services created for easy unit test and SOLID concepts (not all 😁)
14. Messages files for Validations keys
15. Add maven-failsafe-plugin. not run IT on normal build (test class ends withs IT)
16. For run IT tests run *mvn verify* command or runs with IDE support
17. Add rest-assured dependency for api test


### Didn't make
1. Full Crud for Accounts, Products and Payments (Just User made)
2. Test all services (I was foccused on payment flux)

