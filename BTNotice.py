import webbrowser
import urllib,urllib2
import httplib
import datetime,time
import smtplib
import sys
import email.mime.text

def sendmail(value):
    mail_username='haknmcaobin@gmail.com'
    mail_password='11019926zdn'
    from_addr=mail_username
    to_addrs='haknmcaobin@163.com'

    HOST='smtp.gmail.com'
    PORT=587

    smtp=smtplib.SMTP()
    print'connecting...'

    smtp.set_debuglevel(1)

#connect
    try:
        print smtp.connect(HOST,PORT)
    except:
        print'CONNECT ERROR...'

    smtp.starttls()
#login with username & password
    try:
        print'loginning...'
        smtp.login(mail_username,mail_password)
    except:
        print'LOGIN ERROR...'

#fill context with MIMEText obj
    msg=email.mime.text.MIMEText('BitGo game records are attached,\n\n actually there is no attachment because we are not finish the records function yet = =')
    msg['From']=from_addr
    msg['To']=''.join(to_addrs)
    msg['Subject']='BitGo Game Records '#+str(value)

    print msg.as_string()
    smtp.sendmail(from_addr,to_addrs,msg.as_string())
    smtp.quit()

if __name__ == '__main__':
    sta_in=open('value.txt','w')
    for i in range(0):
        logurl='https://vip.btcchina.com/?lang=en#'
        print'Record Sending...'
        print datetime.datetime.now()
        content=urllib2.urlopen(logurl).read()
        value_position=content.find("<td>Bitstamp</td>")
        print content[value_position+36:value_position+43]
        s=content[value_position+36:value_position+43]
        print datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        sta_in.write(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S "))
        sta_in.write(''+content[value_position+36:value_position+43]+'\n')
        time.sleep(3)
    last_value = '' 
    sta_in.close()
    
    sendmail(last_value)
