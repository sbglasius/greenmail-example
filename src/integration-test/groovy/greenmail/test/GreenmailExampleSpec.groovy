package greenmail.test

import com.icegreen.greenmail.util.GreenMailUtil
import grails.plugin.greenmail.GreenMail
import grails.plugins.mail.MailService
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

import javax.mail.internet.MimeMessage

@Integration
class GreenmailExampleSpec extends Specification {

    MailService mailService
    GreenMail greenMail

    void cleanup() {
        greenMail.deleteAllMessages()
    }

    void "send a test mail"() {
        given:
        Map mail = [message: 'hello world', from: 'from@piragua.com', to: 'to@piragua.com', subject: 'subject']

        when:
        mailService.sendMail {
            to mail.to
            from mail.from
            subject mail.subject
            body mail.message
        }

        then:
        greenMail.receivedMessages.length == 1

        with(greenMail.receivedMessages[0]) { MimeMessage message ->
            GreenMailUtil.getBody(message) == mail.message
            GreenMailUtil.getAddressList(message.from) == mail.from
            message.subject == mail.subject
        }
    }

}
