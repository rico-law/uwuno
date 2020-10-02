import React from "react";
import { Checkbox, FormControlLabel, Grid, TextField } from "@material-ui/core";
import { StyledButton as Button } from "../StyledButton/StyledButton";
import "./GameSettingForm.css";

export const GameSettingForm = () => {
  const [state, setState] = React.useState({
    useBlankCards: false,
  });

  const handleChange = (event) => {
    setState({
      ...state,
      [event.target.name]: event.target.checked
    });
  };

  return (
    <form className="ntr-form">
      <Grid
        container
        direction="row"
        justify="space-between"
        alignItems="flex_end"
        className="row"
      >
        <FormControlLabel 
          id="use-blank-cards"
          control={<Checkbox checked={state.useBlankCards} onChange={handleChange} name="useBlankCards" />}
          label="use blank cards"
        />
      </Grid>
      <Grid
        container
        direction="column"
        justify="space-between"
        alignItems="baseline"
        className="row"
      >
        <Button
          variant="contained"
          label="hover over me to view invite link"
          colour="#5BCAEF"
          fullWidth={true}
        />
        <Button
          variant="contained"
          label="start game"
          colour="#53E997"
          fullWidth={true}
        />
      </Grid>
    </form>
  );
};
