import { Component } from "react";
import "./SystemTable.css";

class SystemTable extends Component {
  constructor(props) {
    super(props);

    this.state = {
      systems: props.systems,
    };
  }

  render() {
    // Show systems
    const systems = this.state.systems.map((system) => (
      <tr key={system.id}>
        <td>{system.id}</td>
        <td>{system.name}</td>
        <td>{system.host}</td>
        <td>{system.dmp_endpoint}</td>
        <td>{system.type}</td>
      </tr>
    ));
    return (
      <table>
        <tr>
          <th>ID</th>
          <th>Host</th>
          <th>Name</th>
          <th>Endpoint</th>
          <th>Type</th>
        </tr>
        {systems}
      </table>
    );
  }
}

export default SystemTable;
