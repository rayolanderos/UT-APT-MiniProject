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

class Manage(webapp2.RequestHandler):

    def get(self):
        print(self.uri_for('api-manage'))
        result = urlfetch.fetch(self.uri_for('api-manage', _full=True))

        if result.status_code == 200:
            logout_url = users.create_logout_url('/')
            j = json.loads(result.content)
            page_data = {
            'streams': j, 
            'logout_url': logout_url, 
            'page_name': 'manage'
            }
            template = JINJA_ENVIRONMENT.get_template('manage.html')
            self.response.write(template.render(page_data))
