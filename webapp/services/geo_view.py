import webapp2
import json
import logging
from datetime import datetime, timedelta

from google.appengine.ext import ndb
from google.appengine.api import images


from models.photo import Photo

class GeoView(webapp2.RequestHandler):

    def get(self):
        stream_id = int(self.request.get('id'))
        min_date = int(self.request.get('min'))
        max_date = int(self.request.get('max'))

        days_to_subtract = 365 - max_date 
        formatted_date_max = datetime.today() - timedelta(days=days_to_subtract)

        days_to_subtract = 365 - min_date
        formatted_date_min = datetime.today() - timedelta(days=days_to_subtract)

        self.response.headers['Content-Type'] = 'application/json'
        photos = Photo.query(Photo.stream_id == stream_id, Photo.date <= formatted_date_max, Photo.date >= formatted_date_min ).fetch()

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
