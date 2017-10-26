import webapp2
import json
import logging

from models.photo import Photo
from google.appengine.api import search

class IndexPhotos(webapp2.RequestHandler):

    def get(self):

        photo_query = Photo.query()
        photos = photo_query.fetch()
        photo_list = []

        for photo in photos:
            url = photo.url
            lat = photo.lat
            lon = photo.lon
            stream_id = photo.stream_id

            geopoint = search.GeoPoint(lat, lon)
            search_index = search.Document(
                doc_id= url,
                fields=[search.TextField(name='url', value=url),
                search.TextField(name='stream_id', value=str(stream_id)),
                search.GeoField(name='geopoint', value=geopoint) ])
            result = search.Index(name='photo').put(search_index)
            photo_list.append({'stream_id': stream_id, 'url': url, 'lat': lat, 'lon': lon})

        self.response.out.write(json.dumps(photo_list))
