# train ticket demo app

This is a train ticket demo app with code examples used in the article. It consists of a few business rules that controls which
ticket should be presented based on some input data such as a seat class, travel distance, etc. There are 2 groups of endpoints that 
from the business point of view do the same. First one is a standard Java approach, business rules are created in a form of if..else
statements. The second one is a Hyperon approach.

It's a Spring-boot app, to run just `mvn spring-boot:run`. 8090 is a default port.


## Hyperon endpoints

* `/product/hyperon/v1`
* `/product/hyperon/v2`
* `/product/hyperon/v3`
* `/product/hyperon/v4`

## Standard Java endpoints

* `/product/v1`
* `/product/v2`