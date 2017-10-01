import webapp2
import logging
import sys

from google.appengine.ext import blobstore
from google.appengine.ext.webapp import blobstore_handlers

class PhotoUploadHandler(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        try:
            upload = self.get_uploads()[0]
            self.redirect('/view_photo/%s' % upload.key())

        except:
            e = sys.exc_info()[0]
            logging.info(e)
            self.error(500)


class ViewPhotoHandler(blobstore_handlers.BlobstoreDownloadHandler):
    def get(self, photo_key):
        if not blobstore.get(photo_key):
            self.error(404)
        else:
            self.send_blob(photo_key)