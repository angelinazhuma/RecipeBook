import Link from "next/link";

export default function Navigation() {
    return (
        <nav className="navigation">
            <Link href="/view">
                View recipes
            </Link>

            <Link href="/add">
                Add recipe
            </Link>
        </nav>
    );
}