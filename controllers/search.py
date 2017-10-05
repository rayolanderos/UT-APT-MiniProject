import webapp2
import jinja2
import json
import os
import logging
import urllib

from google.appengine.api import urlfetch
from google.appengine.api import users
from google.appengine.api import search

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class Search(webapp2.RequestHandler):

    def get(self):

        s = self.request.get('s')
        logout_url = users.create_logout_url('/')

        if not s:
            page_data = {
                'results': [],
                's' : '',
                'logout_url': logout_url, 
                'page_name': 'search'
            }
        else : 
            s_safe = urllib.quote_plus(s)
            search_api_uri = '{}?s={}'.format(self.uri_for('api-search', _full=True), s_safe)
            result = urlfetch.fetch(url = search_api_uri)

            if result.status_code == 200:
                results = json.loads(result.content)

                page_data = {
                    'results': results,
                    's' : s,
                    'logout_url': logout_url, 
                    'page_name': 'search'
                }


        template = JINJA_ENVIRONMENT.get_template('search.html')
        self.response.write(template.render(page_data))
