

from google.appengine.ext import ndb

class Stream(ndb.Model):
    name = ndb.StringProperty()
    cover_url = ndb.StringProperty()
