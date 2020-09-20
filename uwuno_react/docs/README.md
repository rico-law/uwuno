# uwuno

## getting started

1. run yarn install in uwuno_react/
2. yarn start

## dependencies

- ws (websockets)
  managing player connections and issuing game events
- MaterialUI
  - @material-ui/core
  - @material-ui/icons
- react-redux
- redux-toolkit
  calls to API to made via async thunks
  information is retrieved from store via selectors
- react-router-dom
- connected-react-router
- react-testing-library

## structure

- docs (you are here!)
- src
  - components
    utilized within pages
  - pages
  - stores
  - testing

## guidelines

- run yarn prettier-format on code before pushing
