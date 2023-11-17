# Car-Rental-System - Microservices :

Car Rental System is a kind of Microservices application where each module performs various tasks such as Account and Car Registration, Login, Booking and Cancellation process and Email Notification Services. Car Rental System application allows user to rent a car for a specific period of time based on the location, price, car model and availability. Filter tray is available to filter out the cars by inputting various options. User can pick or filter a car based on their preferences and can check for slots, Rating and Reviews before proceeding booking process. Once user comfortable with the selected car, User suppose to pick Trip start date and Trip end date. It will provide you status of car availability. If car is available on the selected date, it will further allow you to process the payment gateway ( Razorpay ), User can select their method of paying in order to complete the booking process. Upon successful booking user will get an email confirmation which contains all the necessary information about Car Details, Payment Details and Trip Details along with Car Owner contact information. Cancellation feature is implemented if user feels not to continue the trip. But cancellation will be available one day before trip start date afterwards Cancellation Option will be grayed out. Upon successful cancellation of a trip user amount will be refunded immediately also email confirmation will be received by end user which contains Refund Details, Car Owner Details and Trip Details. For the better visual represenation graphs have been used to show various data such as Number of Cars Rented/Owned, Revenues Received for past 8 years and Current Year, Revenue collected for each car, Rented Count for each car, Top Rated Cars and Transaction Information. Car Owners can edit car location and car details anytime without any restriction. During the car deletion application will make sure if the car is booked by user, If it is booked deletion will not happen. Cancellation contains one additional feautre that allows Car Owner to reject the trip any time if Car Owner is not ready provide car for a trip and amount will be credited to user who booked the car.


# Tech Stack :

**Frontend :** HTML5, CSS3 and Javascript

**Backend :** Core Java and Java 17

**Frameworks :** Spring MVC, Spring Microservices, Spring Hibernate, Spring Security, Rest API, Bootstrap and AJAX

**Libraries :** Jquery

**Databases :** Postgressql and MongoDB

**Messaging System :** RabbitMq


# Microservices :

1. GUI ( For graphical represenation and UI )
2. Rest API ( Developed all neccessary API to perform CRUD Operation )
3. Email Notification Service ( To send booking and cancellation confirmation email, also for OTP verification )


# Features :

**Signup and Register new car :**

1. Account Registration service lets the user to create their new account by submitting the Email ID, User Name, Initial Login Password, Current Location and Mobile Number.
2. Car Registration services lets the user to register the new car into the inventory by submitting car details such as Car Number, Model, Manufacturer, Model Year, Price per day and Location Details.


**Login :**

Login will authenticate and authorize the user by validating credentials, If user is authenticated successfully it further redirects to Dashboard, Else user will get error dialogue box. Login module contains Update/Forgot password option which allows user to update their password by validating email and OTP. Once validations are passed user can update the new password.


**Booking and Cancellation Service :**

1. User can book the car from list of cars and filteration can be performed to reduce the number of cars based on user prefrence. Once car is selected need to input Trip start and end date, If car is available then user can book a car by paying an amount via Razorpay Payment Gateway. Upon successful payment process user will be notified via email id.
2. There is a restriction for Cancelling a trip by user. If user wants to cancel a trip or car they have to be cancelled one day before an actual trip start date. Else Cancellation option will be grayed out. Upon successful cancellation amount will be credited to user account and will be notified via email id.


**Rating and Reviews :**

Rating option will be enabled once trip is completed, user can provide a star rating based on the trip and car experience which helps Car Owner to keep the car well maintained. Any time user can submit their feedback about the car to improve user experience.


**Graphs :**

For the better visual representation graphs have been used to show various data collected by the application, Those are 
1. Revenue - current year, Past 8 years, Each Car
2. Individual Car Rating
3. Number of Cars Rented and Owned
4. Transaction information shows Paid, Unpaid and Cancelled


**Update and Delete :**

Car owner can update the car details and location details any time without any restriction. Also end users can update their profile details. Deleting a registered car will have special condition to check if car is booked by user, If it is booked car will not be deleted, Else deletion will perform.


**Filteration :**

Filter option is available to filter the cars by providing Price range, Model, Year and Location. User can pick the car to book a trip based on filtered results. For the  best user convienience Ratings, Reviews and Slots details can be seen in the Card itself.


**Email Service :**

Email Notification Service is a proof of booked, cancelled trips. Email will be triggered to end user once booking and cancellation process is done. During password reset request to verify user an OTP will be sent to user, User can change password if and only if OTP and Email ID are validated.


# Snap shots from Application :

**Login and Signup**

![Screenshot from 2023-11-15 13-56-14](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/0b8bf8c6-f217-4c4c-90fa-855f9d78e282)

![Screenshot from 2023-11-15 13-56-20](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/a9c8c644-c7f2-4e94-bfbf-a7ebbdc31939)

**Update/Forgot Password**

![updatePasswordBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/7a9e7f04-9a84-4826-a6c0-26548870a331)

![otpEmailBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/3005b227-fcbe-486c-a4ee-bccd5d165214)

![Screenshot from 2023-11-15 18-55-24](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/9c1434f8-b75a-489f-b282-653a0e1f9230)

**Dashboard**

![Screenshot from 2023-11-15 13-57-27](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/43b4e11c-4379-4bce-a02c-a55e8efcbf9b)

**Update Profile**

![updateProfileBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/e73e25f0-dead-468b-94f1-8703caf70da2)

![Screenshot from 2023-11-17 17-31-27](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/191b10df-6d5f-4728-a1e5-a3c9804b3b22)

**Owned Cars ( Lists, Update and Delete )**

![Screenshot from 2023-11-15 13-58-05](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/1f7eab57-4e03-4946-afb1-16a01e6f9fe2)

![Screenshot from 2023-11-15 13-58-11](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/6d917f10-4a37-4d2e-b8ea-81d8e11e318a)

![Screenshot from 2023-11-15 13-58-17](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/6e4101eb-8501-4b80-beaa-720c37e07c88)

**Register New Car**

![Screenshot from 2023-11-15 13-58-25](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/4ed0f57c-ee9c-4161-a67d-42dc36292790)

**Car Rented By Others**

![rentedOwnersBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/f1498b5c-0174-45a5-960d-1c6a11d6dae5)

**Reports and Analytics graphs**

![Screenshot from 2023-11-15 13-59-59](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/ef3bf43d-2237-4305-8fc8-e0a5712ad537)

![Screenshot from 2023-11-15 14-00-15](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/be009c8c-9449-46b2-af12-8725fca2c099)

![Screenshot from 2023-11-15 14-00-25](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/208bab1e-c649-42da-97e6-3aa94caad264)

![Screenshot from 2023-11-15 14-00-32](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/75e81634-46a3-49c9-9c7f-4431bd419ef8)

![Screenshot from 2023-11-15 14-00-38](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/e686895c-c640-497d-b823-bc584cc1999b)

![Screenshot from 2023-11-15 14-00-53](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/ac97643f-426a-4878-bd59-68240c672747)

![Screenshot from 2023-11-15 14-01-02](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/4e27b67a-4bbd-44fc-92a2-3b0ddb912f1e)

**Booking Process**

![Screenshot from 2023-11-15 14-01-19](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/342a8d21-ebc8-491e-840f-7033428e691a)

![Screenshot from 2023-11-15 14-01-47](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/460873b0-1cb9-4edf-a4db-75a4a13518a5)

![Screenshot from 2023-11-15 14-02-04](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/f375d127-2c7b-48ea-87ce-3fd27d645488)

![Screenshot from 2023-11-15 14-02-11](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/9a5bc9e8-e103-40dd-9c54-ff5b37ca690d)

![bookingEmailBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/bc7ad5ff-1963-46b4-bc60-f6429baef29f)

![Screenshot from 2023-11-15 14-02-17](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/0a1de139-9360-4f21-a3ec-d668bc770860)

**Trips ( Upcoming, Ongoing and Past )**

![Screenshot from 2023-11-15 14-03-11](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/5b08438c-e6b8-4f6a-9f50-23d7017c1871)

![Screenshot from 2023-11-15 14-03-18](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/7581a759-8140-454f-af3e-87389e8f1d56)

![Screenshot from 2023-11-15 14-03-25](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/13462341-02a9-4a26-b8a9-4e8041497909)

**Get Slots**

![Screenshot from 2023-11-15 14-02-58](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/ed101eb2-ca89-44a8-926f-639d8772e2f2)

**Filteration Tray**

![Screenshot from 2023-11-15 14-03-03](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/0438f970-f92d-4676-a769-a9e09c6119a5)

**Reviews and Ratings**

![Screenshot from 2023-11-15 14-03-30](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/ccc5a35c-d66f-4f46-aa8b-285ab2468008)

![reviewsBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/f8622d72-95cb-4506-ae89-4fbf89f727f2)

![Screenshot from 2023-11-15 14-04-13](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/3a8b2ac6-aeee-4679-b0bf-c42cd8ac308f)

**Cancellation process**

![Screenshot from 2023-11-17 17-48-27](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/051786f3-7e4f-41a0-9bc7-cb4c11a10b8f)

![cancellationEmailBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/05cb9946-76c3-4b1a-a8da-881cfe3a6bc7)

![Screenshot from 2023-11-15 14-08-50](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/df8f0f7b-4e31-4569-bee8-ad8a0bbf5f9a)

**Transaction Info**

![transactionInfoBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/e347e263-de96-4468-a49f-35dee0b6d681)

**Logout**

![Screenshot from 2023-11-15 14-05-35](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/36cc9268-c51c-410a-b3bd-c7084252abd4)

**Postrgres Schema ( UserAccount and OTPBucket )**

![userAccountInfoBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/fac52f80-c0ee-4a6f-8262-ec3dbbe1f027)

![otpBucketInfoBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/007b5496-7f5d-41d0-90cf-341658b57a8c)

**MongoDB Collections ( Car Inventory and Reservation Details )**

![carInventoryBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/39779c9b-f9ef-4490-a3c3-94ca93dc3b63)

![reservationDetailsBlurred](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/6a7f2b5e-8b1d-4f80-80fb-296620d4a474)

**RabbitMQ**

![Screenshot from 2023-11-15 14-06-02](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/5c45e3f3-7380-4a7b-a42c-f3e429bac9f2)

**Razorpay payment gateway dashboard**

![Screenshot from 2023-11-16 12-49-26](https://github.com/Gangs2000/Car-Rental-System-Microservices/assets/112934529/be18113b-812c-4b8f-84a1-ade620a48ba8)


# Future Implementations :

1. Enhancing user experience by uploading Car Images into Car Inventory which helps end users to have look at the picture before proceeding booking process.
2. End to End Chat Communication between user and car owner.


# Happy Coding :-)



























































