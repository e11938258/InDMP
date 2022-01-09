import { Component } from "react";
import "./Home.css";

import SystemForm from "../../components/SystemForm/SystemForm";
import SystemTable from "../../components/SystemTable/SystemTable";

class Home extends Component {
  constructor(props) {
    super(props);

    this.state = {
      systems: props.systems,
    };
  }

  render() {
    return (
      <div className="page">
        <div className="systems">
          <h2>Registered systems</h2>
          <SystemTable systems={this.state.systems} />
        </div>
        <div className="panel">
          <h2>Control panel</h2>
          <SystemForm />
        </div>
      </div>
    );
  }
}

export default Home;
