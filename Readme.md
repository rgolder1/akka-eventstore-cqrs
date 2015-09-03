Demonstrates interacting with EventStore using Akka, and via ATOM.

Download the EventStore from geteventstore.com

More info:

http://docs.geteventstore.com/introduction/

Run:

./run-node.sh

To run on remote machine:

./run-node.sh --ext-ip=192.x.y.z

Navigate to:
http://127.0.0.1:2113/web/index.html#/
admin:changeit

Interact with curl (to test EventStore operational):

Pub:
curl -i  -d@event.txt http://127.0.0.1:2113/streams/newstream  -H "Content-Type:application/json" -H "ES-EventType: SomeEvent" -H "ES-EventId: C322E299-CB73-4B47-97C5-5054F920746E"

Sub:
curl -i "http://127.0.0.1:2113/streams/newstream" -H "Accept:application/atom+xml"
curl -i http://127.0.0.1:2113/streams/newstream/0 -H "Accept: application/json"

To run demo, navigate to RunAkkaDemo in /src/test/java/ and run the unit test.