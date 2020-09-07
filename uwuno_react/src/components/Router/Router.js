import React from "react";
import { Switch, Redirect, Route, BrowserRouter } from "react-router-dom";
import Home from "../../pages/Home/Home";
import Lobby from "../../pages/Lobby/Lobby";
import Play from "../../pages/Play/Play";

export const Router = () => {
  return (
    <div>
      <BrowserRouter>
        <Switch>
          <Route exact path="/" component={Home} />
          <Route exact path="/play" component={Play} />
          <Route path="/:lobbyId" component={Lobby} />
          <Route
            exact
            path="/not-found"
            render={() => (
              <>
                <h1>Not Found</h1>
                <p>The requested page could not be found</p>
              </>
            )}
          />
          <Redirect to="/not-found" />
        </Switch>
      </BrowserRouter>
    </div>
  );
};
