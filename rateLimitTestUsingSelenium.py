from selenium import webdriver
from time import sleep
test_emails = ["demouser@anyinvaliddomain.com","demouser1@anyinvaliddomain.com"]
url = "https://example.com/register"
driver = webdriver.Chrome()
    
for email in test_emails:
    driver.get(url)
    sleep(3)
    #find first name element by name
    first_name = driver.find_element_by_name("FirstName")
    # submit into element name
    first_name.send_keys("john")
Email = driver.find_element_by_name("Email")
    # submit our fake email IDs
    Email.send_keys(email)
    
    # submit button
    submit_button = driver.find_element_by_id("button_submit")
    submit_button.click()
