import webapp2
import json
import logging
import datetime

from google.appengine.ext import ndb
from google.appengine.api import images


from models.photo import Photo

class GeoView(webapp2.RequestHandler):

    def get(self):
        stream_id = int(self.request.get('id'))

        self.response.headers['Content-Type'] = 'application/json'
        photos = Photo.query(Photo.stream_id == stream_id)

        photos_data = []
        if photos != None :
            for photo in photos:
                photo_data = {
                    'url': photo.get_url(), 
                    'lat': photo.get_lat(), 
                    'lon': photo.get_lon()
                }
                photos_data.append(photo_data)

        else:
            photos_data = None


        self.response.out.write(json.dumps(photos_data))
