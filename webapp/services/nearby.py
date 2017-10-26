import webapp2
import json
import logging
from datetime import datetime, timedelta

from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import search


from models.photo import Photo

class Nearby(webapp2.RequestHandler):

    def get(self):
        lat = int(self.request.get('lat'))
        lon = int(self.request.get('lon'))
        offset = int(self.request.get('offset', 0 ) )
        limit = int(self.request.get('limit', 9 ) ) 

        self.response.headers['Content-Type'] = 'application/json'
        index = search.Index(name='photo')
        user_location = (lat, lon)

        query = "distance(geopoint, geopoint(%f, %f)) >= %f" % (
        user_location[0], user_location[1], 0)
        loc_expr = "distance(geopoint, geopoint(%f, %f))" % (
            user_location[0], user_location[1])
        sortexpr = search.SortExpression(
            expression=loc_expr,
            direction=search.SortExpression.ASCENDING, default_value=45001)
        search_query = search.Query(
            query_string=query,
            options=search.QueryOptions(
                limit= limit,
                offset= offset,
                sort_options=search.SortOptions(expressions=[sortexpr])))
        photos = index.search(search_query)

        photos_data = []
        for photo in photos:
            photo_data = {
                'url': photo.field('url').value, 
                'stream_id': photo.field('stream_id').value
            }
            photos_data.append(photo_data)

        self.response.out.write(json.dumps(photos_data))
