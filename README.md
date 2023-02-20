# Project Names

The purpose of this project is only for learning and self develompent.

* Weather alert by text message.

## Introduction

The aim of this project is to fetch weather forecast data from AccuWeather Api by using 
a serverless function and send it as a text message to all subscribers' emails or mobile phones by sns aws.

## Requirments 

* Java 11
* New Api key from AccuWeather api [https://developer.accuweather.com/user/login?destination=user/me/apps&autologout_timeout=1]
* New AWS account

## Instruction
* Create a new lambda function with Java 11 (Corretto) runtime
* Configurations section in AWS lambda add MAIN_CLASS, "se.WeatherForCast.awspring.AccuWeatherApplication" in Environment variables 
* Runtime settings in AWS lambda add handler :  se.WeatherForCast.awspring.AccuWeatherHandler
* Upload your code as jar file or from 3s bucket.
* Create sns service with a new topic and add your subscriptions
* Replace  apikey in properties.yaml file with yours
* Replace TOPIC_ARN in ApiConstans.java with your subscriptions.
* you can trigger and test the lambda function or create new Events in Cloudwatch with your desired schedule.









