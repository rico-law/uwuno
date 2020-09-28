import React from "react";
import { Grid, TextField } from "@material-ui/core";
import { StyledButton as Button } from "../StyledButton/StyledButton";
import "./EnterGameForm.css";

export const EnterGameForm = () => {
  return (
    <form className="ntr-form">
      <Grid className="row">
        <TextField id="name" label="name" variant="outlined" />
      </Grid>
      <Grid
        container
        direction="row"
        justify="space-between"
        alignItems="baseline"
        className="row"
      >
        <Button
          variant="contained"
          label="join game"
          colour="#5BCAEF"
          fullWidth={false}
        />
        <Button
          variant="contained"
          label="create game"
          colour="#53E997"
          fullWidth={false}
        />
      </Grid>
    </form>
  );
};