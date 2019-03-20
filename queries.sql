--1
insert into customer values

--2

select DISTINCT firstname,lastname from customer, booking 
where status='Cancelled' and customer.LICENCENUMBER=booking.LICENCENUMBER;
-- if you delete, be sure to insert them
delete from booking where status='Cancelled'

--3--
select truck.registration,size from truck,booking 
where truck.registration=booking.registration and notes='request drop off at airport' 
union 
select truck.registration,size from truck,booking 
where truck.registration=booking.registration and notes='request heated seats';

--4


--5
--ADD ANOTHER QUERY HERE.