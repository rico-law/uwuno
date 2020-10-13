import React from "react";
import { Button, ButtonGroup, IconButton, makeStyles } from "@material-ui/core";

const ButtonSubmit = ({
  colour,
  label,
  fullWidth,
  variant = "contained",
}) => {
  const useStyles = makeStyles({
    root: {
      background: colour,
      width: fullWidth ? "100%" : "40%",
    },
    label: {
      textTransform: "lowercase",
      color: "black",
    },
  });

  const classes = useStyles();

  return (
      <Button
        size="large"
        variant={variant}
        classes={{
          root: classes.root,
          label: classes.label,
        }}
      >
        {label}
      </Button>
  );
};

const IncreDecrement = ({
  colour,
  groupColour,
  variant = "contained",
}) => {
  const useStyles = makeStyles({
    root: {
      background: colour,
    },
    label: {
      color: "black",
    },
  });

  const classes = useStyles();

  return (
    <ButtonGroup
      orientation="vertical"
      color={groupColour}
      aria-label="vertical contained primary button group"
      variant={variant}
    >
      <Button
        classes={{
          root: classes.root,
          label: classes.label,
        }}
        variant={variant}
      >
        +
      </Button>
      <Button
        classes={{
          root: classes.root,
          label: classes.label,
        }}
        variant={variant}
      >
        -
      </Button>
    </ButtonGroup>
  );
};

export const StyledButton = {
  ButtonSubmit,
  IncreDecrement
}