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
3. Using Openapi (swagger)
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
15. Tests... (building)

