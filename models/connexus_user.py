from stream import Stream
from google.appengine.ext import ndb

class ConnexusUser(ndb.Model):
    user_id = ndb.StringProperty()
    report = ndb.IntegerProperty()
    streams_subscribed = ndb.KeyProperty(Stream, repeated=True)
