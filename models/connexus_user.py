from stream import Stream
from google.appengine.ext import ndb

class ConnexusUser(ndb.Model):
    user_id = ndb.StringProperty()
    report = ndb.IntegerProperty()
    streams_subscribed = ndb.KeyProperty(Stream, repeated=True)

    @classmethod
    def from_user_id(cls, user_id):
        return cls.query().filter(cls.user_id == str(user_id)).get()