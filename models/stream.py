

from google.appengine.ext import ndb

class Stream(ndb.Model):
    name = ndb.StringProperty()
    cover_url = ndb.StringProperty()
    tags = ndb.StringProperty(repeated=True)
    date = ndb.DateTimeProperty(auto_now_add=True)
    