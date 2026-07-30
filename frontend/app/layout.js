import "./globals.css";

import Navigation from
        "../shared/components/navigation";

export const metadata = {
  title: "RecipeBook",
  description:
      "Save recipes with ingredients",
};

export default function RootLayout({
                                     children,
                                   }) {
  return (
      <html lang="en">
      <body>
      <main className="page">
        <div className="container">
          <header className="header">
            <h1>RecipeBook</h1>

            <p>
              Save recipes with
              ingredients, amounts
              and units
            </p>

            <Navigation />
          </header>

          {children}
        </div>
      </main>
      </body>
      </html>
  );
}