import React from "react";
import { Checkbox, FormControlLabel, Grid } from "@material-ui/core";
import { ButtonSubmit } from "../StyledButton/ButtonSubmit";
import { StyledTextField } from "../StyledTextField/StyledTextField";
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
        alignItems="center"
        className="row"
      >
        <StyledTextField 
          label="number of ais"
          background="#FFFFFF"
          textColour="#000000"
          fullWidth={false}
        />
        {/* TODO: refactor checkbox/formControlLabel into separate component? */}
        <FormControlLabel 
          id="use-blank-cards"
          control={<Checkbox checked={state.useBlankCards} onChange={handleChange} name="useBlankCards"/>}
          label="use blank cards"
        />
      </Grid>
      <Grid
        container
        direction="column"
        justify="space-between"
        alignItems="center"
        className="row"
      >
        <ButtonSubmit
          variant="contained"
          label="hover over me to view invite link"
          colour="#5BCAEF"
          fullWidth={true}
        />
        <ButtonSubmit
          variant="contained"
          label="start game"
          colour="#53E997"
          fullWidth={true}
        />
      </Grid>
    </form>
  );
};
