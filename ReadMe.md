## Liferay MVC Chatbot  "Ask Ray"

This is a chatbot integrated to DialogFlow Google's NLP engine. There are three versions of the chatbot both in my GitHub. This one is a common or garden Liferay MVC Portlet . You can have a conversation with it and it will draw a simple graph using ChartJS below. Ask it "How can I create a blog" by either typing or literally speaking to it. You will need to setup your own DialogFlow chatbot Agent.

Note this module is all about how you use Portlets, how you might integrate with DialogFlow in Liferay and how you might want to authenticate with Google OAuth2.0. I can think of loads of ways 
I might make this module and other modules in this family better, but this is not the point. The code comes with no warranty or support from myself or Liferay. It is a bunch of examples not production code.

## DialogFlow and Google OAuth2.0 service account authentication

The chatbot now uses the V2 version of Google's Dialogflow API which requires OAuth2.0 service account authentication. You must deploy
the **org.apache.servicemix.bundles.gae-1.9.81_1.jar** to Liferay to satisfy this modules dependencies. All other dependencies are satisfied by Liferay
default or as compileInclude.

Because the module uses Google Service account authentication you need to add your json service account file e.g liferay-onhm-1c5c15d292d4.json to /src/main/resources.

The module is a really good example of how you can do server side OAuth2.0 Google API authentication.

You can find out how you create the service account by reading this documentation for Dialogflow [Dialogflow quickstart guide] (https://cloud.google.com/dialogflow/docs/quick/setup)

Documentation on how to setup a DialogFlow Agent can be found here [DialogFlow Documentation] (https://cloud.google.com/dialogflow/docs/basics)

You need to make sure you have an intent setup in DialogFlow called "blog" and you set an action called "blog". Create a page in Liferay and add the blogs widget to it. Then trigger the intent with your training phrase.

The actions are triggered by the Portlet's action phase controller.

## Service dependency

There is a Liferay Service and API which is required as a dependency. This service just records the chat history in Liferay. You need to build and deploy the service, api and chatbot module.

[Ask Ray DialogFlow V2 Service Builder project](https://github.com/kristianp335/ask-ray-liferay-chatbot-dialogflowv2-service)

Make sure you checkout the much cooler React version of this project - [React version](https://github.com/kristianp335/ask-ray-liferay-chatbot-diagflow-portlet-reactjs)

