# Project Names

The purpose of this project is only for learning and self develompent.

* Weather alert by text message.

** Introduction

The aim of this project is to fetch weather forecast data from AccuWeather Api by using 
a serverless function and send it as a text message to all subscribers' emails or mobile phones by sns aws.

** Requirments 

* Java 11
* New Api key from AccuWeather api [https://developer.accuweather.com/user/login?destination=user/me/apps&autologout_timeout=1]
* New AWS account

** Instruction
* Create a new lambda function with java 11
* upload your code as jar file or from 3s bucket.
* Create sns service with new topic and add your subscriptions
* replace TOPIC_ARN in ApiConstans.java with your to make it work with your subscriptions.
* you can trigger and test lambda function or create new Events in Cloudwatch with your scheduled you wants.









