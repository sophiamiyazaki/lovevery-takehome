### Lovevery Take Home Test
This repo is to deliver the contents of the Lovevery Take home assignment which was to build a native Android application in Kotlin that can POST to and GET messages from a service that has a REST interface.

## Approach
I first looked through the assignment to understand the specs. Then I used Postman to hit the 3 API examples to confirm data that will be provided and what the data models look like. I decided to use Jetpack Compose for the UI layer and Retrofit for hitting the API.

Here is the steps I ended up taking:
- Create a blank Kotlin project and confirm device emulator is working
- Think over what will be on the screen
- Create an API interface with the 3 api calls
- Create the needed DTO and Model mapping
- Psuedo MainActivity
  - Create the 3 placeholder functions to post / get data (called by the buttons <— all the Retrofit stuff can end up being abstracted out to another file
- Create Composable for each function
- Set up the http connection with one button to “get all”
  - success with label, button, call returns response
  - if there are no messages, show a message as such
  - deal with UX later
- Next set up the POST
  - Ran into issues hitting the endpoint (intermittently also unavailable via postman) <-- ended up being an emulator issue
- Went back to adding UX now and adding filler method
- Had to clean boot the emulator to fix the endpoint issue
- Responses from _get all_ was different from _get by user_ and _post_ responses so that tripped me up a little until I stepped through the responses I was receiving to troubleshoot
- Cleaned up the UX and added some simple blank input validation

## To consider
If I had more time, or if this was a Production project, I would definitely refactor all the retrofit API calls to a Retrofit helper that provides the retrofit instance. There's a lot of redundant code right now.
I would also add better UX to submit on the Android soft keyboard “done” click as well as better message displays ex: input should meet a requirement, no spaces, alphanumeric with some allowed special chars. I would also need to write up documentation on the API contracts and returned responses.
For now I also punted on authentication if an API requires it; clickstream tracking; and UX design

## Design tradeoffs
In the interest of time, I didn't use any dependency injection which would make testing easier. I also decided to use Jetpack Compose vs XML view files to be more efficient and keep the app small. As mentioned I also kept it simple by having creating a Retrofit instance for each method in the interest of time, but would refactor that out so that a helper can deliver an instance, with a parameter for the url.

## Testing strategy
In terms of testing
- add unit tests for each model within the app
- add UI tests for interactions using the Espresso library
- add integration tests for mocking getting data back from API and displaying it
- add snapshot/screenshot tests for pixel perfect testing if the page has been properly designed
- add accessibility testing (ex: Deque)
- if we add clickstream type click tracking, we should add tests to assert expected output against actual output
