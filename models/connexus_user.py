import logging

from stream import Stream
from google.appengine.ext import ndb

class ConnexusUser(ndb.Model):
    user_id = ndb.StringProperty()
    report = ndb.IntegerProperty()
    streams_subscribed = ndb.KeyProperty(Stream, repeated=True)

    def get_subscribed_streams(self):
        streams = ndb.get_multi(self.streams_subscribed)
        return streams

    def get_report_rate(self):
        report = self.report
        return report

    @classmethod
    def from_user_id(cls, user_id):
        return cls.query().filter(cls.user_id == str(user_id)).get()

    @classmethod
    def is_subscribed(cls, user_id, stream_key):
        stream = cls.query(cls.user_id == str(user_id) and cls.streams_subscribed == stream_key).get()
        return stream != None


