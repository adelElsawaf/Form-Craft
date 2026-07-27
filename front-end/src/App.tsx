import { useTheme } from "@mui/material/styles";

function App() {
  const theme = useTheme();

  return (
    <div
      style={{
        color: theme.palette.primary.main,
        background: theme.palette.background.default,
      }}
    >
      Hello
    </div>
  );
}

export default App
