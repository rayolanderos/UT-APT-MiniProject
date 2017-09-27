import os
import urllib

from google.appengine.api import users

import jinja2
import webapp2

from controllers import view_all
from controllers import manage

from services import view_all as ViewAllService
from services import manage as ManageService
from services import stream_service

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class MainPage(webapp2.RequestHandler):

    def get(self):
        user = users.get_current_user()
        
        if user:
            nickname = user.nickname()
            logout_url = users.create_logout_url('/')
            greeting = 'Welcome, {}! (<a href="{}">sign out</a>)'.format(
                nickname, logout_url)
            view = 'manage.html'
            template_values = {
                'user': user,
                'greeting': greeting,
                'logout_url': logout_url
            }
            self.redirect('/manage')

        else:
            login_url = users.create_login_url('/')
            greeting = 'Plase log in to continue using the app'

            template_values = {
                'greeting': greeting,
                'login_url': login_url
            }

            template = JINJA_ENVIRONMENT.get_template('login.html')
            self.response.write(template.render(template_values))

app = webapp2.WSGIApplication([
    webapp2.Route('/', MainPage, name='home'),
    webapp2.Route('/view_all', view_all.ViewAll, name='view-all'),
    webapp2.Route('/api/view_all', ViewAllService.ViewAll, name='api-view-all'),
    webapp2.Route('/manage', manage.Manage, name='manage'),
    webapp2.Route('/api/manage', ManageService.Manage, name='api-manage'),
    webapp2.Route('/api/create_stream', stream_service.CreateStream, name='api-create-stream')
], debug=True)