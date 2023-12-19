# Ragnarok Spring Api

This repository was created using 
<a href="https://spring.io/projects/spring-boot">spring boot 3.2</a>
to expose resources from ragnarok private servers database, 
based on <a href="https://github.com/rathena/rathena">rathena</a> 
2023 databases.

Our main goal is provide resources to facilitate the construction of 
websites based in restful apis, this will facilitate the development of
sites using moderns frameworks like react or angular.
We also want to provide the maximum of payment gateways integrations 
as is possible, to make the project viable in a lot of countries.
Initially we are integrating the 
<a href="https://www.mercadopago.com.br/developers/">Mercado Pago service</a>
as default payment gateway. This probably will only work in Brazil for now.

We will try to work in 
<a href="https://github.com/HerculesWS/Hercules">HerculeWS</a> database
later. But probably it will work fine without problems, since they database
are very similar.

## Progress

This project is very recent, so we don't have many things for now.

I'm planning to work in the following features:

1) ✅ Users resource endpoints for sign up and sign in
2) ✅ Base integration for Mercado Pago api
3) 💻 (Working in) Mail send
4) 💻 (Working in) Recovery password
5) ❌ Characters resources (example: endpoint to list characters or reset character position)
6) ❌ Donation full integration and npc
7) ❌ Server statics (like users online)
8) ❌ Server status (map:on/char:on/login:on)

## Installation
We are not proving any build for now. You have to download the source and
compile for yourself.

Follow <a href="https://spring.io/projects/spring-boot">spring docs</a> to do it.

I don't have commited my application.properties file, since it have a lot of
private keys, like database key, mercado pago token and others. Instead this, 
I created a file named application.default.properties. Copy it into a new
application.properties and replace the default data with your data.

## Contribute

All contributions are welcome, ranging from people wanting to triage issues, 
others wanting to write documentation, to people wanting to contribute code.
My goals for this open source is that some
devs around the world can integrate some payment gateways from each country,
and also some people can translate the lang folder to other languages.