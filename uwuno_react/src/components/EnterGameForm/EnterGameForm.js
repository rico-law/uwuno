import React from "react";
import { Grid, TextField } from "@material-ui/core";
import { ButtonSubmit } from "../StyledButton/ButtonSubmit";
import "./EnterGameForm.css";

export const EnterGameForm = () => {
  return (
    <form className="ntr-form">
      <Grid className="row">
        <TextField id="name" label="name" variant="outlined" fullWidth={true} />
      </Grid>
      <Grid className="row">
        <TextField id="room-id" label="room id" variant="outlined" fullWidth={true} />
      </Grid>
      <Grid
        container
        direction="row"
        justify="space-between"
        alignItems="baseline"
        className="row"
      >
        <ButtonSubmit
          variant="contained"
          label="join game"
          colour="#5BCAEF"
          fullWidth={false}
        />
        <ButtonSubmit
          variant="contained"
          label="create game"
          colour="#53E997"
          fullWidth={false}
        />
      </Grid>
    </form>
  );
};
