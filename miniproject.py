import os
import json
import urllib

from google.appengine.api import users
from google.appengine.api import urlfetch

import jinja2
import webapp2

import sendgrid
from sendgrid.helpers import mail

from controllers import view_all
from controllers import create_stream
from controllers import manage
from controllers import trending
from controllers import search
from controllers import view
from controllers import photos
from controllers import subscription

from services import view_all as ViewAllService
from services import view as ViewService
from services import manage as ManageService
from services import search as SearchService
from services import trending as TrendingService
from services import generate_trending 
from services import stream_service
from services import update_user_report
from services import send_user_report
from services import create_user
from services import delete as DeleteService

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

INDEX_NAME = 'stream'

class MainPage(webapp2.RequestHandler):

    def try_create_connexxus_user(self):
        user = users.get_current_user()
        create_user_url = self.uri_for('api-create-user', _full=True)
        result = urlfetch.fetch(
            url=create_user_url,
            payload=json.dumps({'user_id': user.user_id(), 'user_email': user.email() }),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )

        if result.status_code != 200:
            logging.error('Not possible to create user :(')

    def get(self):
        user = users.get_current_user()
        
        if user:
            nickname = user.nickname()
            logout_url = users.create_logout_url('/')

            self.try_create_connexxus_user()
            
            template_values = {
                'user': user,
                'logout_url': logout_url
            }
            self.redirect('/manage')

        else:
            login_url = users.create_login_url('/')
            greeting = 'Please log in to continue using the app'

            template_values = {
                'greeting': greeting,
                'login_url': login_url
            }

            template = JINJA_ENVIRONMENT.get_template('login.html')
            self.response.write(template.render(template_values))

app = webapp2.WSGIApplication([
    webapp2.Route('/', MainPage, name='home'),
    webapp2.Route('/view_all', view_all.ViewAll, name='view-all'),
    webapp2.Route('/create_stream', create_stream.Create, name='create-stream'),
    webapp2.Route('/api/view_all', ViewAllService.ViewAll, name='api-view-all'),
    webapp2.Route('/manage', manage.Manage, name='manage'),
    webapp2.Route('/trending', trending.Trending, name='trending'),
    webapp2.Route('/search', search.Search, name='trending'),
    webapp2.Route('/subscribe', subscription.Subscribe, name='subscribe-stream'),
    webapp2.Route('/unsubscribe', subscription.Unsubscribe, name='unsubscribe-stream'),
    webapp2.Route('/api/manage', ManageService.Manage, name='api-manage'),
    webapp2.Route('/api/trending', TrendingService.Trending, name='api-trending'),
    webapp2.Route('/api/search', SearchService.Search, name='api-search'),
    webapp2.Route('/api/generate_trending', generate_trending.GenerateTrending, name='api-generate-trending'),
    webapp2.Route('/api/create_stream', stream_service.CreateStream, name='api-create-stream'),
    webapp2.Route('/api/update_user_report', update_user_report.UpdateUserReport, name='api-update-user-report'),
    webapp2.Route('/api/send_user_report', send_user_report.SendUserReport, name='api-send-user-report'),
    webapp2.Route('/api/delete_stream', DeleteService.DeleteStream, name='api-delete-stream'),
    webapp2.Route('/api/create_user', create_user.CreateUser, name='api-create-user'),
    webapp2.Route('/api/subscribe', stream_service.Subscribe, name='api-subscribe-stream'),
    webapp2.Route('/api/unsubscribe', stream_service.Unsubscribe, name='api-unsubscribe-stream'),
    webapp2.Route('/view', view.View, name='view'),
    webapp2.Route('/api/view', ViewService.View, name='api-view'),
    webapp2.Route('/upload_photo', photos.PhotoUploadHandler, name='upload-photo')

], debug=True)