import React from "react";
import { ButtonGroup, Checkbox, FormControlLabel, Grid, TextField } from "@material-ui/core";
import { StyledButton as Button } from "../StyledButton/StyledButton";
import "./GameSettingForm.css";

export const GameSettingForm = () => {
  const [state, setState] = React.useState({
    useBlankCards: false,
    numAI: 0,
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
        <Button.IncreDecrement
          colour="#FF69B4"
          groupColour="secondary"
        />
        {/* TODO: refactor checkbox/formControlLabel into separate component? */}
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
        <Button.ButtonSubmit
          variant="contained"
          label="hover over me to view invite link"
          colour="#5BCAEF"
          fullWidth={true}
        />
        <Button.ButtonSubmit
          variant="contained"
          label="start game"
          colour="#53E997"
          fullWidth={true}
        />
      </Grid>
    </form>
  );
};
