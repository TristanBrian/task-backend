package Task_Mng.task_backend.util;

public final class HtmlPages {

	private HtmlPages() {
	}

	public static String layout(String title, String body) {
		return """
				<!DOCTYPE html>
				<html lang="en">
				<head>
				  <meta charset="UTF-8">
				  <meta name="viewport" content="width=device-width, initial-scale=1.0">
				  <title>%s</title>
				  <style>
				    body { font-family: system-ui, sans-serif; max-width: 900px; margin: 2rem auto; padding: 0 1rem; color: #1a1a1a; line-height: 1.5; }
				    h1 { margin-bottom: 0.25rem; }
				    a { color: #2563eb; }
				    .muted { color: #6b7280; }
				    .status-up { color: #16a34a; font-weight: 700; }
				    table { width: 100%%; border-collapse: collapse; margin-top: 1rem; }
				    th, td { border: 1px solid #e5e7eb; padding: 0.6rem 0.75rem; text-align: left; }
				    th { background: #f9fafb; }
				    nav { margin: 1rem 0 1.5rem; }
				    code { background: #f3f4f6; padding: 0.1rem 0.35rem; border-radius: 4px; }
				  </style>
				</head>
				<body>
				  <nav><a href="/">Home</a> · <a href="/swagger-ui.html">Swagger UI</a></nav>
				  %s
				</body>
				</html>
				""".formatted(escape(title), body);
	}

	public static String escape(String value) {
		if (value == null) {
			return "";
		}
		return value
				.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;");
	}
}
