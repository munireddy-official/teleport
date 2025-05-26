Scalable Tracking Number Generator API

This is a Spring Boot application that provides a RESTful endpoint to generate unique tracking numbers for shipments based on specified query parameters. The endpoint supports country codes, weight, timestamps, and customer metadata, and returns the tracking number with creation timestamp in RFC 3339 format.

⸻

Features
	•	Generates a unique tracking number (16 characters, alphanumeric)
	•	Accepts RFC 3339 formatted timestamps
	•	Fully RESTful GET API with query parameters
	•	Returns JSON response
	•	Designed to be horizontally scalable

⸻

API Specification

Endpoint

GET /tracker/next-tracking-number

Query Parameters
Parameter              Type            Description
origin_country_id      String          ISO 3166-1 alpha-2 country code (e.g., “MY”)
destination_country_id String          ISO 3166-1 alpha-2 country code (e.g., “ID”)
weight                 Double          Weight in kilograms (up to 3 decimal places)
created_at             String          Timestamp in RFC 3339 format (e.g., 2025-05-26T14:30:00+05:30)
customer_id            UUID            Customer UUID
customer_name          String          Full customer name (e.g., “RedBox Logistics”)
customer_slug          String          Slug/kebab-case customer name (e.g., “redbox-logistics”)

Response

HTTP 200 OK

Content-Type: application/json

{
“trackingNumber”: “A1B2C3D4E5F6G7H8”,
“createdAt”: “2025-05-26T14:30:00+05:30”
}

⸻

Build & Run

Prerequisites
	•	Java 17+
	•	Maven or Gradle

Using Maven
	1.	Build the project:
mvn clean install
	2.	Run the application:
mvn spring-boot:run
⸻

Testing the API

Use Postman or curl:

curl ‘http://localhost:8080/tracker/next-tracking-number?origin_country_id=MY&destination_country_id=ID&weight=1.234&created_at=2025-05-26T14:30:00%2B05:30&customer_id=de619854-b59b-425e-9db4-943979e1bd49&customer_name=RedBox%20Logistics&customer_slug=redbox-logistics’ 
-H ‘Accept: application/json’

⸻

Dependencies
	•	Spring Boot Web
	•	Jackson Datatype JSR310 (for OffsetDateTime)
	•	Java UUID
	•	Java Time (OffsetDateTime)
