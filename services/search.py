import webapp2
import json
import datetime
import logging
import urllib

from google.appengine.api import search

from models.stream import Stream

class Search(webapp2.RequestHandler):

    def get(self):
        self.response.headers['Content-Type'] = 'application/json'
        s = self.request.get('s')

        s = urllib.unquote_plus(s)

        stream_list = []

        expr_list = [search.SortExpression(
            expression='name', default_value='',
            direction=search.SortExpression.DESCENDING)]
        # construct the sort options
        sort_opts = search.SortOptions(
             expressions=expr_list)
        query_options = search.QueryOptions(
            limit=5,
            sort_options=sort_opts)
        query_obj = search.Query(query_string=s, options=query_options)
        results = search.Index(name='stream').search(query=query_obj)

        for result in results:
            stream_list.append({'id': result.field('stream_id').value, 'name': result.field('name').value, 'cover_url': result.field('cover_url').value})


        self.response.out.write(json.dumps(stream_list))
