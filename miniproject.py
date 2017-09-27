import os
import urllib

from google.appengine.api import users

import jinja2
import webapp2

from controllers import view_all
from controllers import create_stream

from services import view_all as ViewAllService
from services import stream_service

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True
)

class MainPage(webapp2.RequestHandler):

    def get(self):
        user = users.get_current_user()

        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        with open('www/login.html') as f:
            self.response.write(f.read())

app = webapp2.WSGIApplication([
    webapp2.Route('/', MainPage, name='home'),
    webapp2.Route('/view_all', view_all.ViewAll, name='view-all'),
    webapp2.Route('/create_stream', create_stream.Create, name='create-stream'),
    webapp2.Route('/api/view_all', ViewAllService.ViewAll, name='api-view-all'),
    webapp2.Route('/api/create_stream', stream_service.CreateStream, name='api-create-stream')
], debug=True)