# Google PubSub Exactly once delivery ACK deadline problem 


With enabled *Exactly once delivery* and *Acknowledgement deadline set to 600s* (=10 minutes), the message receiver still gets message earlier - in 60s or less. 
This [short code](/src/main/java/com/pubsub/exactlyonce/demo/DemoApplication.java) demonstrates the problem.


![img.png](/doc/img.png)

Note the timestamps among messages, **it never reaches 10 minutes of waiting**:
```
08:19:06.277  INFO 26172 --- [sub-subscriber1] GcpTest    : sub1 = global-gcp-pubsub-subscriber1 = HELLO 5185509958571237
08:19:06.278  INFO 26172 --- [sub-subscriber1] GcpTest    : Processing in progress - going to sleep 5 minutes...
08:20:27.810  INFO 26172 --- [sub-subscriber2] GcpTest    : sub1 = global-gcp-pubsub-subscriber2 = HELLO 5185509958571237
08:20:27.810  INFO 26172 --- [sub-subscriber2] GcpTest    : Processing in progress - going to sleep 5 minutes...
08:21:57.854  INFO 26172 --- [sub-subscriber3] GcpTest    : sub1 = global-gcp-pubsub-subscriber3 = HELLO 5185509958571237
08:21:57.855  INFO 26172 --- [sub-subscriber3] GcpTest    : Processing in progress - going to sleep 5 minutes...
```


### How to run the code & steps to reproduce problem
- Create Topic and Subscription according to the screenshot above - ACK deadline 600s, Enabled Exactly-once delivery
- Before running the code, please update `PROJECT_ID` and `SUBSCRIPTION_NAME` in the [code](/src/main/java/com/pubsub/exactlyonce/demo/DemoApplication.java) or set the command line parameter
- Run `gradlew bootRun` optionally `gradlew bootRun -Pargs=projects/$PROJECT_ID/subscriptions/$SUBSCRIPTION_NAME`
- Publish simple message (plain text `HELLO`) to the Subscription you created in the first step 

Related discussion on [StackOverflow](https://stackoverflow.com/questions/73076103/pub-sub-acknowledgement-deadline-is-ignored-when-used-exactly-once-delivery).
