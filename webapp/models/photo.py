import logging

from google.appengine.ext import ndb

class Photo(ndb.Model):
    url = ndb.StringProperty()
    stream_id = ndb.IntegerProperty()
    lat = ndb.FloatProperty()
    lon = ndb.FloatProperty()
    date = ndb.DateTimeProperty(auto_now_add=True)

    def get_lon(self):
        lon = self.lon
        return lon

    def get_lat(self):
        lat = self.lat
        return lat

    def get_url(self):
        url = self.url
        return url

    def get_date(self):
        date = self.date
        return date;

    def get_stream_id(self):
        stream_id = self.stream_id
        return stream_id;

    @classmethod
    def from_url(cls, url):
        return cls.query().filter(cls.url == str(url)).get()


