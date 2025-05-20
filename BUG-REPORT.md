### BUG: POST /pet accepts invalid input

- ID: testPostPet_MissingName_ShouldReturn405
- Input: JSON without "name" field
- Expected: HTTP 400 or 405
- Actual: HTTP 200 OK
- Repro steps:
  - POST /pet with {"id": ..., "photoUrls": ["..."], "status": "available"}
- Impact: server allows broken data
- Suggestion: validate request schema on server side

### Ido tullepes:

[ERROR] postNewPetTest.testPostNewPet_Positive:105 1 expectation failed.
Expected response time was not a value less than <500L> milliseconds, was 596 milliseconds (596 milliseconds).
Expected response time was not a value less than <500L> milliseconds, was 628 milliseconds (628 milliseconds).

### Negativ eseteknel 200-as koddal erkezik a response

- EmptyOrder
  [ERROR] StoreOrderNegativeTest.testEmptyOrder_ShouldFail:81 1 expectation failed.
  Expected status code (<400> or <500>) but was <200>.

- OrderMissingPetId
  [ERROR] StoreOrderNegativeTest.testOrderMissingPetId_ShouldFail:39 1 expectation failed.
  Expected status code (<400> or <500>) but was <200>.

- OrderWithInvalidStatusType
  [ERROR] StoreOrderNegativeTest.testOrderWithInvalidStatusType_ShouldFail:67 1 expectation failed.
  Expected status code (<400> or <500>) but was <200>.

- OrderWithNegativeQuantity
  [ERROR] StoreOrderNegativeTest.testOrderWithNegativeQuantity_ShouldFail:53 1 expectation failed.
  Expected status code (<400> or <500>) but was <200>.
