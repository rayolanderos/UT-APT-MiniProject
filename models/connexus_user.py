from google.appengine.ext import ndb

class ConnexusUser(ndb.Model):
    user_id = ndb.StringProperty()
    report = ndb.IntegerProperty()
