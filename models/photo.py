import logging

from google.appengine.ext import ndb

class Photo(ndb.Model):
    url = ndb.StringProperty()
    stream_id = ndb.IntegerProperty()
    lat = ndb.FloatProperty()
    lon = ndb.FloatProperty()

    def get_lon(self):
        lon = self.lon
        return lon

    def get_lat(self):
        lat = self.lat
        return lat

    def get_url(self):
        url = self.blob_key
        return url

    @classmethod
    def from_url(cls, url):
        return cls.query().filter(cls.url == str(url)).get()


