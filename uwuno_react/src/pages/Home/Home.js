import React from "react";
import {
  Accordion,
  AccordionSummary,
  AccordionDetails,
} from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import { Box } from "../../components/Box/Box";
import { EnterGameForm } from "../../components/EnterGameForm/EnterGameForm";
import "./Home.css";

const accordionStyles = makeStyles(() => ({
  pink: {
    background: "#fed0d2",
  },
}));

function Home() {
  const styles = accordionStyles();

  return (
    <div className="home">
      <h1>uwuno</h1>
      <Box>
        <h3>how to play</h3>
        <p>
          the first player to play all of the cards in their hand in each round
          scores points for the cards their opponents are left holding
        </p>
        <h3>about uwuno</h3>
        <p>
          uwuno is made by angela, illean, rico, & jarrod w a react/spring stack
          4fun xd
        </p>
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            className={styles.pink}
          >
            changelog
          </AccordionSummary>
          <AccordionDetails className={styles.pink}>
            <p>9/7/20 - dark mode added!</p>
          </AccordionDetails>
        </Accordion>
        <EnterGameForm />
      </Box>
    </div>
  );
}

export default Home;
