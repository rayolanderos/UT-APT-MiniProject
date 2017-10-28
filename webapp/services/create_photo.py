import webapp2
import json
import logging

from models.photo import Photo
from google.appengine.api import search

class CreatePhoto(webapp2.RequestHandler):

    def post(self):

        json_string = self.request.body
        dict_object = json.loads(json_string)

        url = dict_object['url']
        stream_id = dict_object['stream_id']
        lat = dict_object['lat']
        lon = dict_object['lon']

        photo_result = Photo.from_url(url)
        
        if not photo_result: 
	        photo = Photo(url=url, stream_id=stream_id, lat=lat, lon=lon)
	        photo_key = photo.put()

        geopoint = search.GeoPoint(lat, lon)
        search_index = search.Document(
            doc_id= str(stream_id),
            fields=[search.TextField(name='url', value=url),
            search.GeoField(name='geopoint', value=geopoint) ])
        result = search.Index(name='photo').put(search_index)
