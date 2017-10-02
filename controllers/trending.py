import webapp2
import jinja2
import json
import os

from google.appengine.api import urlfetch
from google.appengine.api import users

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class Trending(webapp2.RequestHandler):

    def get(self):
        result = urlfetch.fetch(self.uri_for('api-trending', _full=True))
        logout_url = users.create_logout_url('/')
        if result.status_code == 200:
            j = json.loads(result.content)
            streams_data = {'streams': j, 'page_name': 'trending', 'logout_url': logout_url}
            template = JINJA_ENVIRONMENT.get_template('view.html')
            self.response.write(template.render(streams_data))
