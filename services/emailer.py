# using SendGrid's Python Library - https://github.com/sendgrid/sendgrid-python

import sendgrid

SEND_GRID_EMAIL_ADDRESS = 'gleonoliva@gmail.com'
SEND_GRID_API_KEY = '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'

class MailService():

    @classmethod
    def send_email(cls, sender, to, subject, content):
        sg = sendgrid.SendGridClient(SEND_GRID_API_KEY)
        message = sendgrid.Mail()
        message.add_to(to)
        message.set_from(SEND_GRID_EMAIL_ADDRESS) 
        message.set_subject(subject)
        message.set_html(content)

        sg.send(message)
    
