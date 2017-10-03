

from google.appengine.ext import ndb

class Stream(ndb.Model):
    name = ndb.StringProperty()
    cover_url = ndb.StringProperty()
    photos = ndb.BlobKeyProperty(repeated=True)
    tags = ndb.StringProperty(repeated=True)
    date = ndb.DateTimeProperty(auto_now_add=True)
    owner = ndb.StringProperty()
    views = ndb.IntegerProperty()
    views_list = ndb.DateTimeProperty(auto_now_add=False, repeated=True)
    views_list_size = ndb.IntegerProperty()
    trending_rank = ndb.IntegerProperty()
    